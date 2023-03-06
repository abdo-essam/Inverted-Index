# Inverted-Index
find documents that contain specific words or phrases - Information Retrieval

# Faculty Of Computer And Artificial Intelligence Cairo University `FCAI-CU`

## Information Retrieval Assignment

The given Java code builds an inverted index from a set of input files and allows querying the index to find documents that contain specific words or phrases. Here is a high-level explanation of the code:

'DictEntry3' is a class that stores information about a term in the index, including the number of documents that contain the term ('doc_freq'), the total number of occurrences of the term across all documents ('term_freq'), and a 'HashSet' of document IDs ('postingList') where the term appears.
'Index3' is the main class that builds and queries the inverted index. It has two member variables:
'sources': a map that stores the file name for each document in the index.
'index': a hash map that stores the 'DictEntry3' object for each term in the index.
'buildIndex' method takes an array of file names and builds the inverted index by iterating through each file and its lines, tokenizing each line into words, and updating the 'DictEntry3' objects in 'index' accordingly. For each word encountered in the file, the method checks whether it exists in the 'index' hash map. If not, it adds a new 'DictEntry3' object for the word. It then increments the 'doc_freq' count of the 'DictEntry3' object and adds the current document ID to its 'postingList'. Finally, it increments the 'term_freq' count of the 'DictEntry3' object. After processing all the files, the method prints the size of the dictionary.
'find' method takes a string of terms as input and returns the file names that contain all the terms. It first tokenizes the input string into words and gets the posting list of the first word (converted to lowercase) from 'index'. It then iteratively intersects this posting list with the posting lists of the other words in the input string. The final result is the list of file names associated with the document IDs in the intersection. If there is no intersection, the method returns an empty string.
'intersect' method takes two 'HashSet' objects representing two posting lists and returns their intersection.
Note that the code does not perform any text preprocessing, such as stemming or stop word removal, which are common steps in building an inverted index for information retrieval. It also assumes that the input files are text files and not binary files.

### For More info (https://www.educative.io/answers/what-is-an-inverted-index)
