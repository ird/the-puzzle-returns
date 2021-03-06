import requests
import re

"""
card.py - demonstrates a partial solution to the KnightPuzzle
(ird/the-puzzle-returns) problem. Key function bodies have been
removed so as to present a challenge / learning framework.

card_complete.py is the complete solution
"""


def main():
    """
    Builds the word tree (see word_tree()) and starts a new puzzle.
    Enters a loop until all 5 rounds are complete, each time getting
    a new grid (stored in 'grid'). Calls search(), which does all the
    work. Nothing to change here.
    """
    words = []
    with open("words.txt") as f:
        lines = f.readlines()
        for w in lines:
            words.append(w.upper()[:-1])
    T = word_tree(words)
    url = "http://ec2-35-178-2-160.eu-west-2.compute.amazonaws.com/puzzle"
    token = get_token(url)  # start a new puzzle
    while True:
        grid = get_grid(url, token)
        results = []
        search(T['N'], grid, "N", grid.index("N"), results)
        print("Possible solutions: ", results)
        done = False
        for r in results:
            msg = respond(url, token, r)
            if msg != "Bzzzt.":
                print(msg)
            if re.match("\s+I", msg):  # we know what the answer looks like!
                done = True
                break
        if done:
            break


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


def word_tree(words):
    """
    This builds a tree according to a list of words to later be searched
    whilst looking for valid words in search(...)
    Example tree for NANDO, NATTY, NANNY and NERDS
    {'N':
      {'A': {'N': {'D': {'O': {}},
                   'N': {'Y': {}}},
             'T': {'T': {'Y': {}}}},
       'E': {'R': {'D': {'S': {}}}}}}
    """
    tree = {'N': {}}
    for w in words:
        T = tree[w[0]]
        for depth in range(1, len(w)):
            if w[depth] not in T.keys():
                T[w[depth]] = {}
            T = T[w[depth]]
    return tree


def knight_moves(start):
    """
    Returns a list of positions that are legal knight moves
    from 'start' on a chessboard that is represented as an list(64)
    """
    rel_pos = [(-1, -2), (1, -2), (-2, -1), (2, -1),
               (-2, 1), (2, 1), (-1, 2), (1, 2)]
    res = []
    for (x, y) in rel_pos:
        adjustment = x + y*8
        pos = start + adjustment
        # print("trying pos=", pos, "adj=", adjustment)
        if pos % 8 != x + start % 8:
            # print("\t horizontal wrap")
            continue
        if pos > 0 and pos < 64:
            res.append(pos)
    return res


def test():
    """
    Unit tests.
    Test 2 depends on having puzzle running at http://localhost:80/puzzle
    """
    # 1) does word_tree(words) work?
    Tr = {'N':
          {'A': {'N': {'D': {'O': {}},
                       'N': {'Y': {}}},
                 'T': {'T': {'Y': {}}}},
           'E': {'R': {'D': {'Y': {}}}}}}
    assert(Tr == word_tree(['NANDO', 'NATTY', 'NANNY', 'NERDY']))
    # 2) does get_puzzle(url) work?
    url = "http://localhost/puzzle"
    token = get_token(url)
    grid = get_grid(url, token)
    assert(token is not None and grid is not None)
    # 3) does respond(token, answer) work?
    assert(respond(url, token, "NATTY") == "Bzzzt.")
    # 4) does search(tree, grid, word, pos, res) work?
    grid = ("O H B O Y T H I "
            "S I S H A R D ! "
            "S K F D W G Y S "
            "R S X Y L H L Q "
            "R B S S R E L V "
            "J W K N B R A X "
            "K X I F Y J S B "
            "A O B F E D D X").split(" ")
    res = []
    search(Tr['N'], grid, "N", grid.index("N"), res)
    assert(res == ['NERDY'])
    print("All tests passed")


def get_token(url):
    """
    retrieve puzzle session from url
    return the id (token) of the session
    """
    html = requests.get(url).text
    # parse token from hidden field
    token = int(re.search(
        "type=\"hidden\" value=\"([0-9]+)", html).group(1))
    return token


def get_grid(url, token):
    """
    retrieve puzzle session from url with 'token'
    return the corrosponding grid
    """
    html = requests.get(url, {'token': token}).text
    # parse puzzle string from html body
    puzzle_match = re.search(
        "monospace;\"><br/>(.*)<sp.*</span>(.*)</label>", html)
    puzzle_str = puzzle_match.group(1) + "N" + puzzle_match.group(2)
    puzzle_str = "".join(puzzle_str.split("<br/>"))[:-1]  # trim space
    return puzzle_str.split(" ")


def respond(url, token, answer):
    """
    POST response back and parse and return the response
    """
    resp = requests.post(url, {'token': token, 'answer': answer})
    # try and match a message if there is one
    msg_match = re.search("<label>(.*)</label>\s+</div>\s+</fi", resp.text)
    if msg_match:
        msg = msg_match.group(1)
    else:
        # extract the text between the <body> tags
        msg = resp.text.split("<body>")[1].split("</body>")[0]
        # remove <br/> tags
        msg = "".join(msg.split("<br />"))
    return msg


if __name__ == '__main__':
    main()  # TODO: if you want to run test instead of main(), change it here
