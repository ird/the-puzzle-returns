# KnightPuzzle

RESTful web application that generates a random 'KnightPuzzle' using Spring and MongoDB. 
Players must answer a number of rounds within a time limit to win.

![KnightPuzzle]( "Knight Puzzle")

# Building
Use gradle jar or gradle bootRun

# Solving
KnightPuzzle includes a python DFS tool to search for possible answers in the grid, defined in search() (solution/solution.py)
```python
def search(tree, grid, word, pos, results):
    """
    Performs a recursive depth-first search of the grid using
    knight_moves(pos) to pick the next moves. If grid[position]
    is a child of the current tree, then recursively search that
    child tree too.
    Everytime a word of 5 characters is made, add it to 'res', but
    check it is not already in it first.
    """
    for m in knight_moves(pos):
        if grid[m] in tree.keys():
            if len(word) == 4:
                if (word + grid[m]) not in results:
                    results.append(word + grid[m])
	    search(tree[grid[m]], grid, word + grid[m], m, results)
```
