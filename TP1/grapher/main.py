import sys

if __name__ == '__main__':
    argv = sys.argv
    if len(argv) > 3:
        results_file = argv[1]
        static_file = argv[2]
        dynamic_file = argv[3]
    else:
        raise ValueError(f'Please provide all files.')

    try:
        static_info = open(static_file, 'r')
        dynamic_file = open(dynamic_file, 'r')
        results = open(results_file, 'r')
        lines = results.readlines()




        for line in lines:
            print(line)

    except FileNotFoundError:
        raise ValueError(f'{results_file} file not found.')

