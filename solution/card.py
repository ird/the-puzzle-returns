def solve(puzzle_string, word_tree):
    """ solve the birthday puzzle, get the pies """
    puzzle = puzzle_string.split(" ")
    assert(len(puzzle) == 64)
    start_pos = puzzle.index('N')
    valid_moves = [start_pos]
    next_start = 0
    # search the board
    sol = ['N', '', '', '', '']
    for i in range(1, 5):
        new_starts = len(valid_moves)
        for s in range(next_start, new_starts):
            for m in knight_moves(valid_moves[s]):
                if path(word_tree, sol, puzzle[m]):
                    sol[i] += puzzle[m]
                valid_moves.append(m)
        next_start = new_starts
    print(sol)


def path(tree, partial, letter):
    return True


def build_solution_tree(file):
    """
    Example tree for NANDO, NATTY, NANNY and NERDS
    tree = {'N':{
                'A':
                    {'N':
                        {'D': {'O': {}}
                         'N': {'Y': {}},
                     'T':
                        {'T': {'Y': {}}},
                'E': {...}}}

    """
    words = ['NANDO', 'NATTY', 'NANNY', 'NERDS']
    tree = {'N': {}}
    return tree


def knight_moves(start):
    """
    returns a list of positions that are legal
    knight moves from p on a chessboard that is
    represented as an list(64)
    """
    rel_pos = [(-1, -2), (1, -2), (-2, -1), (2, -1),
               (-2, 1), (2, 1), (-1, 2), (1, 2)]
    res = []
    for (x, y) in rel_pos:
        adjustment = x + y*8
        pos = start + adjustment
        # print("trying position=", pos, "rel adj=", adjustment)
        if pos % 8 != x + start % 8:
            # print("\t horizontal wrap")
            continue
        if pos > 0 and pos < 64:
            res.append(pos)
    return res


def get_puzzle(url):
    """ retrieve puzzle from url and strip html tags """
    pass


def respond(url, token, answer):
    """ POST response back and return the response """
    pass


def test():
    # does build_solution_tree(words) work?
    print(build_solution_tree(""))
    # does get_puzzle(url) work?
    # does respond(token, answer) work?
    s = ("O H B O Y T H I "
         "S I S H A R D ! "
         "S K F D W G Y S "
         "R S X Y L H L Q "
         "R B S S R E L V "
         "J W K N B B A X "
         "K X I F B J S B "
         "A O B F A D O X")
    # does solve(puzzle_string) work?
    assert(solve(s, {}) == "NABOB")


if __name__ == '__main__':
    test()
