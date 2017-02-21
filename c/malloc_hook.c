#include <stdlib.h>
#include <stdio.h>

#define __USE_GNU
#include <dlfcn.h>

typedef void* (*real_malloc_t) (size_t);

static real_malloc_t real_malloc = NULL;

static void __mtrace_init(void)
{
    real_malloc = (real_malloc_t) dlsym(RTLD_NEXT, "malloc");
    if (NULL == real_malloc) {
        fprintf(stderr, "Error in `dlsym`: %s\n", dlerror());
        return;
    }
}

void *malloc(size_t size)
{
    if(real_malloc == NULL)
        __mtrace_init();

    void *p = NULL;
    fprintf(stderr, "malloc(%zu) = ", size);
    p = real_malloc(size);
    fprintf(stderr, "%p\n", p);
    return p;
}


int main(int argc, char **argv, char **env)
{
    void * nothing = malloc(666);
    free(nothing);
}
