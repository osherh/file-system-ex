Data Structures i used:

1. dirMap - a map that maps between the directory name and the DirectoryEntity
2. fileMap - a map that maps between the file name and the FileEntity
3. fileMaxHeap - a max heap(implemented as a priority queue) that stores file entities
4. each directory has a set for its children(subdirectories and files)

note - all the data structures on my solution are thread-safe which is important for multi-threaded enviornments.

I will use these terms:
D - total directories on the file system
F - total files on the file system
d - total directories on the current directory
f - total files on the current directory

-------------------------------------------------------------------------------------------
Time Complexity Analysis:
-------------------------------------------------------------------------------------------

1. addFile(String parentDirName, String fileName, long fileSize)
	Find the parent directory: O(1) on average
	Add file to the directory’s children: O(f+d) (since CopyOnWriteArraySet is used)
	Insert file into fileMap: O(1) on average
	Insert file into fileMaxHeap: O(log F)

	Overall Complexity: O(log F + f + d)

2.	void addDir(String parentDirName, String dirName)

	find the parent directory: O(1) on average
	Add directory to the parent’s children: O(f+d) (since CopyOnWriteArraySet is used)
	Insert the directory into dirMap: O(1)
	
	Overall Complexity: O(f + d)

3. getFileSize(String fileName)

	Lookup file in fileMap: O(1) on average
	
	Overall Complexity: O(1) on average

4. getBiggestFile()

	Retrieve(not removing) the max element from PriorityBlockingQueue (heap): O(1)
	
	Overall Complexity: O(1)

5. showFileSystem()

	Overall Complexity: O(F + D)  (traversing the entire file system)

6. delete(String name)

	Check if the name exists in fileMap or dirMap: O(1)
	
	If it is a file
		Remove the file from fileMap: O(1) on average
		Remove file from fileMaxHeap: O(F) (since PriorityBlockingQueue doesn't support the removal of a specific file efficiently)
		Remove file from parent directory: O(f) (since CopyOnWriteArraySet is used)
		* i think its time complexity would be lower if FileEntity would have a pointer to its parent dir, to DirectoryEntity
		
		Overall Complexity: O(F + f)
		
	If it is a directory:
		deleteDirectoryRecursively() iterates over all the children: O(f + d)
		
		Overall Complexity: O(f + d)

-------------------------------------------------------------------------------------------
Space Complexity Analysis:
-------------------------------------------------------------------------------------------

1. addFile(String parentDirName, String fileName, long fileSize)

	Inserts a new FileEntity into fileMap: O(1)
	Adds it to the parent directory's children: O(1)
	Adds to fileMaxHeap: O(1)
	
	Overall Complexity: O(1)

2. addDir(String parentDirName, String dirName)

	Inserts a new DirectoryEntity in dirMap: O(1) in average
	Adds a reference in the parent directory’s children: O(1)
	
	Overall Complexity: O(1)

3. getFileSize(String fileName)

	Overall Complexity: O(1)

4. getBiggestFile()

	Overall Complexity: O(1)

5. showFileSystem()

	Uses recursion for hierarchy traversal.
	recursion depth: O(D)
	
	Overall Complexity: O(D)

6. delete(String name)

If it is a file:
	Removes it from fileMap: O(1)
	Removes it from fileMaxHeap: O(1)
	Removes from the parent directory’s children: O(1)

	Overall Complexity: O(1)

If it is a directory:
	recursion depth: O(D)

	Overall Complexity: O(D)
