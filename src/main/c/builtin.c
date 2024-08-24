#define bool _Bool
#define MAX_INPUT_LENGTH 1024
#define MAX_INT_STR_LENGTH 12
typedef __SIZE_TYPE__ size_t;

int printf(const char *format, ...);

int sprintf(char *dest, const char *format, ...);

int scanf(const char *format, ...);

int sscanf(const char *src, const char *format, ...);

size_t strlen(const char *str);

int strcmp(const char *str1, const char *str2);

void *memcpy(void *dest, const void *src, size_t n);

void *malloc(size_t n);

typedef __builtin_va_list va_list;
#define va_start(ap, param) __builtin_va_start(ap, param)
#define va_end(ap)          __builtin_va_end(ap)
#define va_arg(ap, type)    __builtin_va_arg(ap, type)

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

void *_mallocArray(size_t size, size_t length) {
  size_t *a = (size_t *) malloc(size * length + sizeof(size_t));
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

void *v_allocArrayMulti(size_t size, size_t depth, size_t num, size_t *a) {
  // use recursive function to allocate multi-dimensional array
  if (depth == 1) {
    return _mallocArray(size, a[0]);
  }
  void *array = _mallocArray(sizeof(void *), a[0]);
  if (num == 1) {
    return array;
  }
  for (size_t i = 0; i < a[0]; i++) {
    void *subArray = v_allocArrayMulti(size, depth - 1, num - 1, a + 1);
    ((void **) array)[i] = subArray;
  }
  return array;
}

// depth is used to determine whether we should allocate a sub-array or the actual data
// in fact in both situations the size is 4 as we are on a 32-bit machine and all the types are assumed to have size 4
void *_allocArrayMulti(size_t size, size_t depth, size_t num, ...) {
  va_list ap;
  size_t *a = (size_t *) malloc(sizeof(size_t) * num);
  va_start(ap, num);
  for (size_t i = 0; i < num; i++) {
    a[i] = va_arg(ap, size_t);
  }
  va_end(ap);
  return v_allocArrayMulti(size, depth, num, a);
}