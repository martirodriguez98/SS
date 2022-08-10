import sys

if __name__ == '__main__':
    print("hello!")
    argv = sys.argv
    if len(argv) > 1:
        results_file = argv[1]
    else:
        raise ValueError(f'Please provide a file with results.')

    try:
        stream = open(results_file, 'r')
        print(stream)
    except FileNotFoundError:
        raise ValueError(f'{results_file} file not found.')

