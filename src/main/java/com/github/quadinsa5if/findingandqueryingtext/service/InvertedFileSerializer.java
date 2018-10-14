package com.github.quadinsa5if.findingandqueryingtext.service;

import com.github.quadinsa5if.findingandqueryingtext.exception.InvalidInvertedFileException;
import com.github.quadinsa5if.findingandqueryingtext.model.Entry;
import com.github.quadinsa5if.findingandqueryingtext.model.vocabulary.Vocabulary;
import com.github.quadinsa5if.findingandqueryingtext.model.vocabulary.implementation.InDiskVocabularyImpl;
import com.github.quadinsa5if.findingandqueryingtext.model.vocabulary.implementation.InMemoryVocabularyImpl;
import com.github.quadinsa5if.findingandqueryingtext.model.ReversedIndexIdentifier;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface InvertedFileSerializer {

    /**
     * Serialize and write the vocabulary to the disk
     *
     * @param vocabulary The InMemoryVocabularyImpl to serialize
     * @return The while on the disk
     */
    File serialize(InMemoryVocabularyImpl vocabulary);

    /**
     * Unserialize the vocabulary from a inverted file
     *
     * @param file The inverted file
     * @param header  The offset map
     * @return The InMemoryVocabularyImpl structure
     */
    InMemoryVocabularyImpl unserialize(File file, Map<String, ReversedIndexIdentifier> header) throws InvalidInvertedFileException;

    Map<String, ReversedIndexIdentifier> unserializeHeader(File file);

    List<Entry> unserializePostingList(File file, int postingListOffset, int postingListLength);


}
