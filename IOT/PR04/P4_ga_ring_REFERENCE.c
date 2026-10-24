/*
 * P4 - A ring of genetic algorithm subpopulations with MPI
 * P4_ga_ring_REFERENCE.c - COMPLETE SOLUTION, FOR THE TEACHER ONLY
 *
 * This is the file the students receive with the eight function bodies
 * removed. Everything else is identical. The seed of the random numbers
 * is 1234 + rank * 77 plus the number given on the command line, so the
 * five seeds of stages two and three give five different runs. Process 0
 * reports the best value of the ring and the number of evaluations after
 * which the maximum was first reached, which is the measure of stages two
 * and three.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mpi.h>

#define N_VARS        2
#define BITS_PER_VAR  16
#define L             (N_VARS * BITS_PER_VAR)
#define POP_SIZE      64
#define P_CROSS       0.8
#define P_MUT         (1.0 / (double) L)
#define MIGRATE_EVERY 20
#define MIGRATE_BEST  1
#define MAX_EVALS     20000
#define X_MIN         0.0
#define X_MAX         65535.0
#define TAG_MIGRATE   7

typedef struct { char gene[L]; double fit; } Individual;

static double rnd(void) { return (double) rand() / ((double) RAND_MAX + 1.0); }

double decode(const char *gene, int var)
{
    unsigned long v = 0;
    int i, base = var * BITS_PER_VAR;
    for (i = 0; i < BITS_PER_VAR; i++)
        v = (v << 1) | (unsigned long) gene[base + i];
    return X_MIN + (double) v * (X_MAX - X_MIN) / (double) ((1UL << BITS_PER_VAR) - 1);
}

double fitness(const char *gene)
{
    double x = decode(gene, 0), y = decode(gene, 1);
    return x * x + y * y;
}

int tournament(const Individual *pop)
{
    int a = (int) (rnd() * POP_SIZE), b = (int) (rnd() * POP_SIZE);
    return (pop[a].fit >= pop[b].fit) ? a : b;
}

void crossover_two_point(const char *p1, const char *p2, char *c1, char *c2)
{
    int i, a, b, t;
    memcpy(c1, p1, L); memcpy(c2, p2, L);
    if (rnd() >= P_CROSS) return;
    a = (int) (rnd() * L); b = (int) (rnd() * L);
    if (a > b) { t = a; a = b; b = t; }
    for (i = a; i < b; i++) { c1[i] = p2[i]; c2[i] = p1[i]; }
}

void mutate(char *gene)
{
    int i;
    for (i = 0; i < L; i++)
        if (rnd() < P_MUT) gene[i] = (char) (1 - gene[i]);
}

void insert_sorted(Individual *pop, const Individual *ind)
{
    int i, j;
    if (ind->fit < pop[POP_SIZE - 1].fit) return;
    for (i = 0; i < POP_SIZE; i++) if (ind->fit > pop[i].fit) break;
    if (i >= POP_SIZE) i = POP_SIZE - 1;          /* equal to the worst */
    for (j = POP_SIZE - 1; j > i; j--) pop[j] = pop[j - 1];
    pop[i] = *ind;
}

void migrate_select(const Individual *pop, Individual *out)
{
    *out = MIGRATE_BEST ? pop[0] : pop[(int) (rnd() * POP_SIZE)];
}

void migrate_accept(Individual *pop, const Individual *in)
{
    if (in->fit >= pop[POP_SIZE - 1].fit) insert_sorted(pop, in);
}

static int cmp(const void *a, const void *b)
{
    double d = ((const Individual *) b)->fit - ((const Individual *) a)->fit;
    return (d > 0) - (d < 0);
}

int main(int argc, char *argv[])
{
    int rank, size, next, prev, i, seed, evals = 0, gen = 0, evals_best = 0;
    double maximum = 2.0 * X_MAX * X_MAX, best_so_far;
    Individual pop[POP_SIZE], c1, c2, out, in;
    MPI_Status st;
    struct { double fit; int rank; } mine, best;
    struct { int evals; int rank; } m_first, first;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    next = (rank + 1) % size;
    prev = (rank - 1 + size) % size;
    seed = (argc > 1) ? atoi(argv[1]) : 0;        /* ./P4_ga_ring 3 uses seed 3 */
    srand(1234 + rank * 77 + seed);

    for (i = 0; i < POP_SIZE; i++) {
        int k; for (k = 0; k < L; k++) pop[i].gene[k] = (char) (rnd() < 0.5);
        pop[i].fit = fitness(pop[i].gene);
    }
    qsort(pop, POP_SIZE, sizeof(Individual), cmp);
    best_so_far = pop[0].fit;

    while (evals < MAX_EVALS) {
        int i1 = tournament(pop), i2 = tournament(pop);
        crossover_two_point(pop[i1].gene, pop[i2].gene, c1.gene, c2.gene);
        mutate(c1.gene); mutate(c2.gene);
        c1.fit = fitness(c1.gene); c2.fit = fitness(c2.gene);
        evals += 2;
        insert_sorted(pop, &c1);
        insert_sorted(pop, &c2);
        gen++;
        if (gen % MIGRATE_EVERY == 0 && size > 1) {
            migrate_select(pop, &out);
            MPI_Sendrecv(&out, sizeof(Individual), MPI_BYTE, next, TAG_MIGRATE,
                         &in,  sizeof(Individual), MPI_BYTE, prev, TAG_MIGRATE,
                         MPI_COMM_WORLD, &st);
            migrate_accept(pop, &in);
        }
        if (pop[0].fit > best_so_far) { best_so_far = pop[0].fit; evals_best = evals; }
    }

    printf("process %d best %.0f at x=%.0f y=%.0f, first reached after %d evaluations\n",
           rank, pop[0].fit, decode(pop[0].gene, 0), decode(pop[0].gene, 1), evals_best);
    fflush(stdout);

    mine.fit = pop[0].fit; mine.rank = rank;
    MPI_Reduce(&mine, &best, 1, MPI_DOUBLE_INT, MPI_MAXLOC, 0, MPI_COMM_WORLD);
    m_first.evals = (pop[0].fit >= maximum) ? evals_best : MAX_EVALS + 1;
    m_first.rank = rank;
    MPI_Reduce(&m_first, &first, 1, MPI_2INT, MPI_MINLOC, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        printf("\nbest of the whole ring: %.0f, found by process %d\n"
               "the maximum possible is %.0f\n", best.fit, best.rank, maximum);
        if (first.evals <= MAX_EVALS)
            printf("the maximum was first reached by process %d after %d of its %d evaluations\n",
                   first.rank, first.evals, MAX_EVALS);
        else
            printf("the maximum was not reached\n");
    }

    MPI_Finalize();
    return 0;
}
