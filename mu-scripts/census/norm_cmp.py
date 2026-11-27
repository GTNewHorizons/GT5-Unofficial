"""Compares two census map files ignoring line order and the order of items within each output and chance list."""
import re
import sys


def norm(line):
    def sort_list(m):
        return m.group(1) + "[" + ",".join(sorted(x.strip() for x in m.group(2).split(","))) + "]"
    return re.sub(r"(out=|oc=|fout=)\[([^\]]*)\]", sort_list, line.rstrip("\n"))


a = sorted(norm(l) for l in open(sys.argv[1], encoding="utf-8"))
b = sorted(norm(l) for l in open(sys.argv[2], encoding="utf-8"))
print(sys.argv[1].split("/")[-1], "identical" if a == b else "DIFFERENT %d vs %d" % (len(set(a) - set(b)), len(set(b) - set(a))))
