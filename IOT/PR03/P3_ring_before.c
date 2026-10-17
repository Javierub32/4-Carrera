/*
 * P3 - A word around a ring with MPI
 * P3_ring.c - starting code
 *
 * Process 0 reads a word from the keyboard and sends it to process 1.
 * Every process prints that it has the word and passes it to the next one.
 * When the word returns to process 0 the program reports it and ends.
 *
 * This file contains THREE deliberate defects, of three different kinds:
 *   - one that the compiler rejects
 *   - one that appears the first time you run it
 *   - one that appears only when you use more than two processes
 *
 * Above each correction, write a comment saying what was wrong and how you
 * noticed it.
 */

#include <stdio.h>
#include <string.h>
#include <mpi.h>
#include <unistd.h>

#define MAXWORD 64
#define TAG     1

int main(int argc, char *argv[])
{
    int rank, size, next, prev;
    char word[MAXWORD];
    MPI_Status status;

    MPI_Init(&argc, &argv);

	// The first param of MPI_Comm_rank and MPI_Comm_size should be MPI_COMM_WORLD, not MPI_COMM_ALL.
	// I noticed this because the program was not compiling and gave an error about MPI_COMM_ALL being undefined.
	// I changed MPI_COMM_ALL to MPI_COMM_WORLD in both calls.
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

	// "Next" calculation was incorrect because it did not calculate the modulus correctly.
	// I notices because the terminal output was showing "invalid rank".
	// I changed next from "rank + 1 " to "(rank + 1) % size"
    next = (rank + 1) % size;
    prev = (rank - 1 + size) % size;

	sleep(2);

    if (rank == 0) {
        printf("type a word and press enter: ");
        fflush(stdout);
        if (scanf("%63s", word) != 1) {
            printf("no word was read\n");
            MPI_Finalize();
            return 1;
        }
        printf("process 0 sends the word %s\n", word);
        fflush(stdout);

        MPI_Send(word, MAXWORD, MPI_CHAR, next, TAG, MPI_COMM_WORLD);
        MPI_Recv(word, MAXWORD, MPI_CHAR, prev, TAG, MPI_COMM_WORLD, &status);

        printf("the word %s came back to process 0, the ring is closed\n", word);
        fflush(stdout);
    } else {
		// The MPI_Recv call was incorrect because it was receiving from process 0 instead of the previous process.
		// I noticed this because the program did not finish correctly when using more than two processes.
		// I changed the source parameter from 0 to prev in the MPI_Recv call
        MPI_Recv(word, MAXWORD, MPI_CHAR, prev, TAG, MPI_COMM_WORLD, &status);

        printf("process %d has the word %s\n", rank, word);
        fflush(stdout);

        MPI_Send(word, MAXWORD, MPI_CHAR, next, TAG, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}