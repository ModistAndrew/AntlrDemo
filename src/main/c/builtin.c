#define MAX_INPUT_LENGTH 1024
#define MAX_INT_STR_LENGTH 12
#define MAX_BYTE_SIZE 4

typedef _Bool bool;
typedef __SIZE_TYPE__ size_t;
typedef __builtin_va_list va_list;

#define va_start(ap, param) __builtin_va_start(ap, param)

#define va_end(ap)          __builtin_va_end(ap)

#define va_arg(ap, type)    __builtin_va_arg(ap, type)

int scanf(const char *format, ...);

int sscanf(const char *src, const char *format, ...);

int printf(const char *format, ...);

int sprintf(char *dest, const char *format, ...);

// we use malloc instead of calloc to avoid the overhead of zeroing the memory
void *malloc(size_t n);

void *memcpy(void *dest, const void *src, size_t n);

size_t strlen(const char *str);

int strcmp(const char *str1, const char *str2);

int _array_size(void *array) {
  return (int) ((size_t *) array)[-1];
}

int string_length(char *str) {
  return (int) strlen(str);
}

char *string_substring(char *str, size_t start, size_t end) {
  size_t len = end - start;
  char *sub = (char *) malloc((len + 1) * sizeof(char));
  memcpy(sub, str + start, len);
  sub[len] = '\0';
  return sub;
}

int string_parseInt(char *str) {
  int n;
  sscanf(str, "%d", &n);
  return n;
}

int string_ord(char *str, size_t i) {
  return (int) str[i];
}

void print(char *str) {
  printf("%s", str);
}

void println(char *str) {
  printf("%s\n", str);
}

void printInt(int n) {
  printf("%d", n);
}

void printlnInt(int n) {
  printf("%d\n", n);
}

char *getString() {
  char *str = (char *) malloc(MAX_INPUT_LENGTH * sizeof(char));
  scanf("%s", str);
  return str;
}

int getInt() {
  int n;
  scanf("%d", &n);
  return n;
}

char *toString(int n) {
  char *str = (char *) malloc(MAX_INT_STR_LENGTH * sizeof(char));
  sprintf(str, "%d", n);
  return str;
}

char *_toStringBool(bool b) {
  return b ? "true" : "false";
}

void *_mallocClass(size_t size) {
  return malloc(size);
}

void *_mallocArray(size_t length) {
  size_t *a = (size_t *) malloc(MAX_BYTE_SIZE * length + sizeof(size_t));
  a[0] = length;
  return a + 1; // store the length before the first element
}

int _compareString(char *str1, char *str2) {
  return strcmp(str1, str2);
}

char *_concatString(char *str1, char *str2) {
  size_t len1 = strlen(str1);
  size_t len2 = strlen(str2);
  size_t len = len1 + len2;
  char *str = (char *) malloc((len + 1) * sizeof(char));
  memcpy(str, str1, len1);
  memcpy(str + len1, str2, len2);
  str[len] = '\0';
  return str;
}

char *_concatStringMulti(size_t num, ...) {
  va_list ap;
  va_start(ap, num);
  size_t len = 0;
  for (size_t i = 0; i < num; i++) {
    char *str = va_arg(ap, char *);
    len += strlen(str);
  }
  va_end(ap);
  char *str = (char *) malloc((len + 1) * sizeof(char));
  size_t pos = 0;
  va_start(ap, num);
  for (size_t i = 0; i < num; i++) {
    char *s = va_arg(ap, char *);
    size_t l = strlen(s);
    memcpy(str + pos, s, l);
    pos += l;
  }
  va_end(ap);
  str[len] = '\0';
  return str;
}

// use recursive function to allocate multi-dimensional array
void *v_mallocArrayMulti(size_t num, va_list dimensions) {
  size_t dimension = va_arg(dimensions, size_t);
  void *array = _mallocArray(dimension);
  if (num == 1) {
    return array;
  }
  for (size_t i = 0; i < dimension; i++) {
    void *subArray = v_mallocArrayMulti(num - 1, dimensions);
    ((void **) array)[i] = subArray;
  }
  return array;
}

void *_mallocArrayMulti(size_t num, ...) {
  va_list ap;
  va_start(ap, num);
  void *array = v_mallocArrayMulti(num, ap);
  va_end(ap);
  return array;
}