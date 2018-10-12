package com.github.quadinsa5if.findingandqueryingtext.service.implementation;

import com.github.quadinsa5if.findingandqueryingtext.lang.Iter;
import com.github.quadinsa5if.findingandqueryingtext.model.ArticleId;
import com.github.quadinsa5if.findingandqueryingtext.model.Entry;
import com.github.quadinsa5if.findingandqueryingtext.model.vocabulary.implementation.InMemoryVocabularyImpl;
import com.github.quadinsa5if.findingandqueryingtext.service.InvertedFileSerializer;
import com.github.quadinsa5if.findingandqueryingtext.service.QuerySolver;

import java.io.File;
import java.util.*;

public class FaginSolvertImpl implements QuerySolver {

  private final InMemoryVocabularyImpl vocabulary;

  public FaginSolvertImpl(File invertedFile) {
    final InvertedFileSerializer serializer = new InvertedFileSerializer() {
      @Override
      public File serialize(InMemoryVocabularyImpl vocabulary) {
        return null;
      }

      @Override
      public InMemoryVocabularyImpl unserialize(File file) {
        return null;
      }
    };
    vocabulary = serializer.unserialize(invertedFile);
  }

  @Override
  public Iter<ArticleId> answer(String[] terms, int k) {
    return null;
  }

  public void topKQuery(String[] terms, int k) {

    final Map<String, List<Entry>> scoreSortEntries = new HashMap<>();
    final Map<String, Map<Integer, Entry>> randomAccessEntries = new HashMap<>();
    for (String term: terms) {
      final List<Entry> sortedEntriesForTerm = Optional.ofNullable(vocabulary.data.get(term))
          .orElse(new ArrayList<>());
      final Map<Integer, Entry> randomAccessEntriesForTerm = Optional.ofNullable(randomAccessEntries.get(term))
          .orElse(new HashMap<>());

      scoreSortEntries.put(term, sortedEntriesForTerm);
      for (Entry entry : sortedEntriesForTerm) {
        randomAccessEntriesForTerm.put(entry.articleId.id, entry);
      }
      randomAccessEntries.put(term, randomAccessEntriesForTerm);
    }

    final List<Entry> M = new ArrayList<>();
    final List<Entry> C = new ArrayList<>();

    while (C.size() != k) {

    }

  }

  public void sortedAccess(Map<String, List<Entry>> scoreSortEntries) {



  }

}