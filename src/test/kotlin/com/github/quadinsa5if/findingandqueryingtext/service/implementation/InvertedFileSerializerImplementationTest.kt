package com.github.quadinsa5if.findingandqueryingtext.service.implementation

import com.github.quadinsa5if.findingandqueryingtext.model.Entry
import com.github.quadinsa5if.findingandqueryingtext.model.HeaderAndInvertedFile
import com.github.quadinsa5if.findingandqueryingtext.model.vocabulary.implementation.InMemoryVocabularyImpl
import com.github.quadinsa5if.findingandqueryingtext.service.InvertedFileSerializer
import com.github.quadinsa5if.findingandqueryingtext.util.NaiveCompressor
import com.github.quadinsa5if.findingandqueryingtext.util.VByteCompressor
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.File
import java.io.FileReader
import java.io.RandomAccessFile

@RunWith(JUnitPlatform::class)
object InvertedFileSerializerImplementationTest: Spek({

  val TEST_FOLDER_NAME = "tmp_test/if"
  val testDirectory = File(TEST_FOLDER_NAME)

  if (!testDirectory.exists()) {
    testDirectory.mkdirs()
  } else {
    testDirectory.listFiles().forEach { it.delete() }
  }

  val serializer: InvertedFileSerializer = InvertedFileSerializerImplementation(TEST_FOLDER_NAME, NaiveCompressor())

  val d1 = 1
  val d2 = 2
  val d3 = 3
  val d4 = 4
  val d5 = 5
  val d6 = 6

  val postingListT1 = listOf(
      Entry(d2, .9f),
      Entry(d5, .8f),
      Entry(d6, .7f),
      Entry(d4, .6f),
      Entry(d1, .5f),
      Entry(d3, .4f)
  )

  val postingListT2 = listOf(
      Entry(d3, .85f),
      Entry(d5, .8f),
      Entry(d2, .75f),
      Entry(d6, .74f),
      Entry(d1, .74f),
      Entry(d4, .7f)
  )

  val postingLists = InMemoryVocabularyImpl()
  postingListT1.forEach { postingLists.putEntry("t1", it) }
  postingListT2.forEach { postingLists.putEntry("t2", it) }

  var _i = 0
  fun autoInc(): Int {
    return _i++
  }

  fun genFile() = File(TEST_FOLDER_NAME + "/gen_file_" + autoInc())

  fun getHeaderAndInvertedFile() = HeaderAndInvertedFile(genFile(), genFile())

  given("an inverted file serializer implementation") {
    on("serialize") {
      val result = serializer.serialize(postingLists, getHeaderAndInvertedFile())
      it("result should be ok") {
        assertThat(result.attempt().isOk, equalTo(true))
      }
    }
  }

  given("the serialized files") {
    val result = serializer.serialize(postingLists, getHeaderAndInvertedFile()).sync()
    val headerFile = result.headerFile!!
    val invertedFile = result.invertedFile!!

    on("unserialize header") {
      val headerResult = serializer.unserializeHeader(FileReader(headerFile)).attempt()
      it("result must be ok") {
        assertThat(headerResult.isOk, equalTo(true))
      }
    }

    on("unserialize vocabulary from header") {
      val headerResult = serializer.unserializeHeader(FileReader(headerFile)).attempt()
      val vocResult = serializer.unserialize(RandomAccessFile(invertedFile, "r"), headerResult.unwrap()).attempt()
      it("result must be ok") {
        assertThat(vocResult.isOk, equalTo(true))
      }
      it("result me be equals to the serialized posting list") {
        assertThat(vocResult.unwrap(), equalTo(postingLists))
      }
    }
  }

})