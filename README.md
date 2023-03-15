# Inverted-Index
find documents that contain specific words or phrases - Information Retrieval

# Faculty Of Computer And Artificial Intelligence Cairo University `FCAI-CU`

## Information Retrieval Assignment

This code implements an inverted index to support text search. The inverted index is built from a set of documents, where each document is represented by a unique integer ID. The code defines two classes: DictEntry3 and Index3.

DictEntry3 defines an entry in the inverted index. It has three fields:

- doc_freq: the number of documents that contain the term.
- term_freq: the number of times the term is mentioned in the collection.
- postingList: a set of document IDs where the term occurs.




Index3 defines the inverted index. It has two fields:

- sources: a map from document ID to the file name.
- index: the inverted index, which is a map from terms to DictEntry3 objects.




The Index3 class has several methods:

- buildIndex: builds the inverted index from a set of files. It loops through each file, reads it line by line, splits each line into words, and updates the inverted index accordingly. It also adds the document ID and file name to the sources map.

- printDictionary: prints the inverted index. It loops through each entry in the index map and prints the term, doc_freq, term_freq, and posting list.
- find: finds documents that contain a set of query terms. It takes a string of query terms as an argument, splits it into words, and intersects the posting lists of          each term to find the documents that contain all the query terms. It then returns the file names of these documents.

- intersect: computes the intersection of two posting lists.

- union: computes the union of two posting lists.



Overall, this code provides a basic implementation of an inverted index and supports simple text search. However, it has some limitations, such as not handling stop words, stemming, or phrase queries. Additionally, it does not have any relevance ranking or scoring mechanism.

### For More info (https://www.educative.io/answers/what-is-an-inverted-index)
