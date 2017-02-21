#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

typedef uint32_t bitfield_t;
enum { BITFIELD_BITS = sizeof(bitfield_t) * 8 };

static inline int bindex(int b) { return b / BITFIELD_BITS; }
static inline int boffset(int b) { return b % BITFIELD_BITS; }
#define bptr(st) (&((st)->bits))

struct _Bits
{
    uint32_t nw;
    bitfield_t bits;
};

typedef struct _Bits * Bits;

static inline Bits bits_alloc(int n)
{
    uint32_t nw = (n / BITFIELD_BITS);
    if (nw * BITFIELD_BITS < n) nw += 1;
    int sz = sizeof(int) + (nw * sizeof(bitfield_t));
    /* printf("wants %d bits : alloc %d bytes (1 uint32_t[%lu]  + %d bitfields[%lu])\n", n, sz, sizeof(uint32_t), nw, (nw * sizeof(bitfield_t))); */
    Bits b = calloc(1, sz);
    /* memset(b, 0, sz); */
    b->nw = nw;
    return b;
}

static inline void bits_free(Bits bits)
{
    free(bits);
}

static inline void set_bit(Bits bits, int b)
{
    bptr(bits)[bindex(b)] |= (bitfield_t)(1 << boffset(b));
}

static inline void reset_bit(Bits bits, int b)
{
    bptr(bits)[bindex(b)] &= ~(1 << boffset(b));
}

static inline int get_bit(Bits bits, int b)
{
    int of = boffset(b);
    return (bptr(bits)[bindex(b)] & (1 << of)) >> of;
}

static inline Bits bits_copy(Bits dst, Bits src)
{
    if (dst->nw != src->nw) return 0;
    return memcpy(&dst->bits, &src->bits, (sizeof(bitfield_t) * src->nw));
}

static void assert(int value, int expected, char*msg)
{
    if (value != expected) {
        fprintf(stderr, "Error %s\n", msg);
        exit(1);
    }
}

int main(int argc, char **argv, char **env)
{
    printf("bits in an bitfield_t : %d\n", BITFIELD_BITS);
    Bits a = bits_alloc(33);
    Bits b = bits_alloc(33);
    set_bit(a, 31);
    set_bit(a, 32);
    set_bit(a, 33);
    assert(get_bit(a, 30), 0, "bit 30 is not 0");
    assert(get_bit(a, 31), 1, "bit 31 is not 1");
    assert(get_bit(a, 32), 1, "bit 32 is not 1");
    assert(get_bit(a, 33), 1, "bit 33 is not 1");
    assert(get_bit(a, 34), 0, "bit 34 is not 0");
    reset_bit(a, 32);
    assert(get_bit(a, 30), 0, "bit 30 is not 0");
    assert(get_bit(a, 31), 1, "bit 31 is not 1");
    assert(get_bit(a, 32), 0, "bit 32 is not 0");
    assert(get_bit(a, 33), 1, "bit 33 is not 1");
    assert(get_bit(a, 34), 0, "bit 34 is not 0");
    set_bit(a, 32);
    reset_bit(a, 33);
    reset_bit(a, 31);
    assert(get_bit(a, 30), 0, "bit 30 is not 0");
    assert(get_bit(a, 31), 0, "bit 31 is not 0");
    assert(get_bit(a, 32), 1, "bit 32 is not 1");
    assert(get_bit(a, 33), 0, "bit 33 is not 0");
    assert(get_bit(a, 34), 0, "bit 34 is not 0");
    bits_copy(b, a);
    assert(get_bit(b, 30), 0, "bit 30 is not 0");
    assert(get_bit(b, 31), 0, "bit 31 is not 0");
    assert(get_bit(b, 32), 1, "bit 32 is not 1");
    assert(get_bit(b, 33), 0, "bit 33 is not 0");
    assert(get_bit(b, 34), 0, "bit 34 is not 0");
    bits_free(a);
    bits_free(b);
    printf("Success\n");
    return 0;
}
