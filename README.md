# Huffman-Codec<br/>

Huffman Codec is basically a compression decompression application built
in JAVA that uses huffman tree for encoding data. So, first the frequency table is built by reading the
input file. Now, in order to build the huffman tree, we built a priority queue interface implementing
3 types of priority queue namely binary heap, 4-way heap and pairing heap and tested which one works
fastest and used that. I used the extract min function in the priority queue to build the huffman tree
and used depth first search to traverse and build the code table and encoded data as per this table.
For the decoding part, I built the decode tree using code table and decoded the encoded file using
this tree.