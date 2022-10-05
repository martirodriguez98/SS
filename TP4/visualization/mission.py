from export_ovito import export_to_ovito

if __name__ == '__main__':
    static_file = "static_file.txt"
    dynamic_file = "results.txt"
    export_path = "ovito_results.dump"
    export_to_ovito(static_file, dynamic_file, export_path)


