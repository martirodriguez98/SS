from ovito_visualization import export_to_ovito


def ejA():
    static_file = "static.txt"
    dynamic_file = "dynamic.txt"
    L = 70
    W = 20
    D = 3

    export_to_ovito(static_file, dynamic_file,L,W,D,"ovito.dump")


if __name__ == '__main__':
    ejA()
