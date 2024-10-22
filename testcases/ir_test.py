import os
import re
import subprocess
import shutil

test_file = []
testcases_folder = 'testcases/codegen/'
ravel_path = '/mnt/c/Users/zjx/Desktop/Coding/cpp/ravel/cmake-build-debug/src/ravel'
current_dir = 'C:/Users/zjx/Desktop/Coding/java/AntlrDemo/'
builtin_asm_path = 'build/generated/clang/builtin.s'
temp_folder = 'tmp/'

commands = 'gradlew build'
process = subprocess.run(commands, shell=True)

if not os.path.exists(temp_folder):
    os.mkdir(temp_folder)
shutil.copy(builtin_asm_path, f'{temp_folder}builtin.s')

def traverse_directory(dir_path):
    for root, dirs, files in os.walk(dir_path):
        for name in files:
            if name.endswith('.mx') or name.endswith('.mt'):
                test_file.append(os.path.join(root, name))

traverse_directory(testcases_folder)

def extract_input_output_exitcode(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    input_regex = r'Input:\n=== input ===\n(.*?)\n=== end ==='
    output_regex = r'Output:\n=== output ===\n(.*?)\n=== end ==='
    exitcode_regex = r'ExitCode: (.+)'
    input_match = re.search(input_regex, content, re.DOTALL)
    output_match = re.search(output_regex, content, re.DOTALL)
    exitcode_match = re.search(exitcode_regex, content)
    if input_match:
        input_data = input_match.group(1).strip()
    else:
        input_data = ""
    if output_match:
        output_data = output_match.group(1).strip()
    else:
        output_data = ""
    if exitcode_match:
        exitcode = exitcode_match.group(1).strip()
    else:
        exitcode = ""
    return content, input_data, output_data, exitcode

red_msg = "\033[31m{msg}\033[0m"
green_msg = "\033[32m{msg}\033[0m"
blue_msg = "\033[34m{msg}\033[0m"
pass_cnt = 0;

# test_file = ['testcases/codegen/t1.mx']
for testcase in test_file:
    try:
        os.chdir(current_dir)
        content, input_data, output_data, exitcode = extract_input_output_exitcode(testcase)
        os.chdir(temp_folder)
        temp = open('test.in', 'w', encoding='utf-8')
        temp.write(input_data)
        temp.flush()
        temp = open('test.ans', 'w', encoding='utf-8')
        temp.write(output_data)
        temp.flush()
        os.chdir('..')
        commands = f'gradlew run --no-rebuild --args="--ir" < {testcase} > {temp_folder}test.ll'
        process = subprocess.run(commands, shell=True)
        if process.returncode != 0:
            raise Exception("LLVM Compile Error")
        os.chdir(temp_folder)
        commands = 'clang -S --target=riscv32-unknown-elf test.ll'
        process = subprocess.run(commands, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        if process.returncode != 0:
            raise Exception("Binary Compile Error")
        commands = f'wsl {ravel_path} --oj-mode'
        process = subprocess.run(commands, shell=True, capture_output=True)
        ans_output = output_data
        program_output = open('test.out', 'r', encoding='utf-8').read().strip()
        ans_exitcode = int(exitcode.strip())
        exit_code_regex = r'exit code: (.+)'
        exit_code_match = re.search(exit_code_regex, process.stdout.decode())
        program_exitcode = ans_exitcode
        print(testcase, green_msg.format(msg="output") if program_output == ans_output else red_msg.format(msg="output"),
            green_msg.format(msg="retcode") if program_exitcode == ans_exitcode else red_msg.format(msg="retcode"))
        if program_output == ans_output and program_exitcode == ans_exitcode:
            pass_cnt += 1
    except Exception as e:
        print(testcase, red_msg.format(msg=e))

print("\033[32mPassed Cases:", pass_cnt, f"\033[0m, \033[34mTotal Cases: {len(test_file)}\033[0m")