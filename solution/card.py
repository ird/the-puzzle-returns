import requests
import re

"""
  _    _                           ____  _      _   _         _
 | |  | |                         |  _ \(_)    | | | |       | |
 | |__| | __ _ _ __  _ __  _   _  | |_) |_ _ __| |_| |__   __| | __ _ _   _
 |  __  |/ _` | '_ \| '_ \| | | | |  _ <| | '__| __| '_ \ / _` |/ _` | | | |
 | |  | | (_| | |_) | |_) | |_| | | |_) | | |  | |_| | | | (_| | (_| | |_| |
 |_|  |_|\__,_| .__/| .__/ \__, | |____/|_|_|   \__|_| |_|\__,_|\__,_|\__, |
              | |   | |     __/ |                                      __/ |
              |_|   |_|    |___/                                      |___/
              (in 3 days)
              Dear Linda
              Have a birthday puzzle!
              Ho ho

Some docs that may help:
* https://docs.python.org/3/tutorial/datastructures.html#dictionaries
* http://www.diveintopython.net/native_data_types/lists.html
* https://docs.python.org/3/tutorial/introduction.html#strings
* depth-first search
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
        res = []
        search(T['N'], grid, "N", grid.index("N"), res)
        print("Possible solutions: ", res)
        done = False
        for r in res:
            msg = respond(url, token, r)
            if msg != "Bzzzt.":
                print(msg)
            if re.match("\s+I", msg):
                done = True
                break
        if done:
            break


def search(tree, grid, word, pos, res):
    """
    Performs a recursive depth-first search of the grid using
    knight_moves(pos) to pick the next moves. If grid[position]
    is a child of the current tree, then recursively search that
    child tree too.

    Everytime a word of 5 characters is made, add it to 'res', but
    check it is not already in it first.
    """
    for m in knight_moves(pos):
        # TODO - see if the letter represented by m is in the tree
        # if it is, recursively search
        # add to res if an answer is found
        pass  # remove me!


def word_tree(words):
    """
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
            # TODO: if current letter (w[depth]) isn't in T, add it
            pass  # remove me!
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
        adjustment = 0  # TODO: calculate relative adjustment
        pos = start + adjustment
        # print("trying pos=", pos, "adj=", adjustment)
        if pos % 8 != x + start % 8:
            # print("\t horizontal wrap")
            continue
        if pos > 0 and pos < 64:
            # TODO: add to res
            pass  # remove me!
    return res


"""
#####################################################################
###     Unit tests - useful to see how the functions work...      ###
#####################################################################
"""


def test():
    # does word_tree(words) work?
    Tr = {'N':
          {'A': {'N': {'D': {'O': {}},
                       'N': {'Y': {}}},
                 'T': {'T': {'Y': {}}}},
           'E': {'R': {'D': {'Y': {}}}}}}
    assert(Tr == word_tree(['NANDO', 'NATTY', 'NANNY', 'NERDY']))
    # does get_puzzle(url) work?
    url = "http://ec2-35-178-2-160.eu-west-2.compute.amazonaws.com/puzzle"
    token = get_token(url)
    grid = get_grid(url, token)
    assert(token is not None and grid is not None)
    # does respond(token, answer) work?
    assert(respond(url, token, "NATTY") == "Bzzzt.")
    # does search(tree, grid, word, pos, res) work?
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


"""
#####################################################################
###    Get/submit helper functions - not part of the solution     ###
#####################################################################
"""


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
    main()  # TODO: if you want to run test() instead of main(), change it here
