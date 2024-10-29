import os

def count_lines_of_code(directory):
    total_lines = 0
    for root, _, files in os.walk(directory):
        for file in files:
            with open(os.path.join(root, file), 'r', encoding='utf-8') as f:
                total_lines += sum(1 for _ in f)
    return total_lines

# Specify the directory and file extensions to include
project_directory = 'C:/Users/zjx/Desktop/Coding/java/AntlrDemo/src/main'
total_lines = count_lines_of_code(project_directory)
print(f'Total lines of code: {total_lines}')