/*
 * P2 - A concurrent server in C
 * P2_conc_server.c - starting code
 *
 * A TCP server that answers every client with the number of connections it
 * has served so far, then closes. Each client is attended by its own child
 * process, so a slow client does not block the others.
 *
 * TWO THINGS TO DO WITH THIS FILE:
 *
 *   1) One region is missing. It is marked below with the comment
 *      ==== YOUR CODE GOES HERE ====
 *      Read what that region has to achieve and write it.
 *
 *   2) The rest of the file contains THREE deliberate defects, of three
 *      different kinds: one that the compiler rejects, one that appears the
 *      first time you run it, and one that appears only when a client
 *      behaves in a particular way. They are outside the region you write,
 *      so a failure is either yours or theirs, and you can always tell.
 *
 * Above each correction, write a comment saying what was wrong and how you
 * noticed it.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT     9100
#define BUFSIZE  256

/* Attends one client. Runs inside the child process. */
static void serve_client(int conn_fd, int served)
{
    char buffer[BUFSIZE];
    int n;

    n = snprintf(buffer, BUFSIZE,
                 "hello, you are client number %d\n", served);

    sleep(5);                      /* pretend the work takes time */

    /* the client may already have gone away while we were working */
    write(conn_fd, buffer, n);
    sleep(1);
    write(conn_fd, "goodbye\n", 8);

    close(conn_fd);
    printf("server: child %d finished with client %d\n", getpid(), served);
    fflush(stdout);
}

int main(void)
{
    int listen_fd, conn_fd;
    struct sockaddr_in serv_addr, cli_addr;
    socklen_t cli_len;
    int served = 0;

	// The SIGPIPE signal is sent to a process when it attempts to write to a socket that has been closed by the other end.
	// This can happen if the client disconnects before the server has finished sending data.
	// By ignoring the SIGPIPE signal, we do not let the child process terminate when the client disconnects.
	// I noticed it because the server child processes were terminating unexpectedly when the client disconnected before the server finished sending data.
	signal(SIGPIPE, SIG_IGN);

    listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) {
        perror("socket");
        exit(1);
    }

    /* The port is released for immediate reuse. Without this, a server that
       has just been stopped leaves the port reserved for about a minute and
       the next run fails with "Address already in use". This is given to
       you: it is not one of the defects. */
    {
        int yes = 1;
        if (setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR,
                       &yes, sizeof(yes)) < 0) {
            perror("setsockopt");
            exit(1);
        }
    }

    memset(&serv_addr, 0, sizeof(serv_addr));
	// The family direction did not match the socket type. 
	// It was AF_UNIX, but it should be AF_INET.
	// I noticed this because the program throws the error: "bind: Address family not supported by protocol".
    serv_addr.sin_family      = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port        = htons(PORT);

    if (bind(listen_fd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("bind");
        exit(1);
    }

    if (listen(listen_fd, 8) < 0) {
        perror("listen");
        exit(1);
    }

    printf("server: listening on port %d, my process number is %d\n",
           PORT, getpid());
    fflush(stdout);

    for (;;) {
        cli_len = sizeof(cli_addr);
		// The accept() call third argument was missing.
		// Now it could save the client address size in cli_len.
        conn_fd = accept(listen_fd, (struct sockaddr *) &cli_addr, &cli_len);
        if (conn_fd < 0) {
            perror("accept");
            continue;
        }

        served++;
        printf("server: client %d connected from %s\n",
               served, inet_ntoa(cli_addr.sin_addr));
        fflush(stdout);
		
        pid_t child_pid = fork();

        if (child_pid == 0) {
            close(listen_fd);
            serve_client(conn_fd, served);
            exit(0);
        } else if (child_pid > 0) {
            close(conn_fd);
        } else {
            perror("fork");
            close(conn_fd);
        }
    }

    return 0;
}