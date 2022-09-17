from ovito_visualization import export_to_ovito


def test():
    N = 30
    L = 6
    static_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/static_{N}.txt'
    dynamic_file = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/algorithm/src/main/resources/dynamic_{N}.txt'
    export_path = f'/Users/martirodriguez/Documents/ITBA/SS/SS/TP3/visualization/results/ovito_{N}.dump'
    export_to_ovito(static_file, dynamic_file,L, export_path)


if __name__ == '__main__':
    test()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
