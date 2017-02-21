#include <stdint.h>
#include <stdio.h>

typedef union __attribute__ ((__packed__))
{
    struct __attribute__ ((__packed__))
    {
        unsigned int table_id      : 11;
        unsigned int int_table_id  : 11;
        unsigned int entry_id      : 12;
        unsigned int generation    : 30;
    };
    uintptr_t i;
} BITS;

uintptr_t compose(uintptr_t a, uintptr_t b, uintptr_t c, uintptr_t d)
{
    BITS i;
    i.table_id = a;
    i.int_table_id = b;
    i.entry_id = c;
    i.generation = d;
    return i.i;
}

int main(int argc, char **argv, char **env)
{
    uintptr_t t = compose(666,777,888,999);
    printf("%d %d %d %d -> 0x%lu\n", 666, 777, 888, 999, t);

    return 0;
}
