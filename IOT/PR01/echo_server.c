/*
 * P1 - Echo over TCP in C
 * P1_echo_server.c - starting code
 *
 * A TCP server that returns to the client exactly the bytes it received.
 *
 * This file contains THREE deliberate defects:
 *   - one stops it from compiling
 *   - one stops it from running
 *   - one lets it run, and lets small messages work, but is still wrong
 *
 * Find them, fix them, and write a short comment above each fix saying
 * what was wrong and how you noticed it.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

// The port number wasn't an usable port number. Ports from 0 to 1023 are reserved.
// I changed it to 12345, which is a valid port number for regular users.
#define PORT 12345

#define BUFSIZE  1024

int main(void)
{
    int listen_fd, conn_fd;
    struct sockaddr_in serv_addr, cli_addr;
    socklen_t cli_len;
    char buffer[BUFSIZE];
    ssize_t n;

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
    serv_addr.sin_family      = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	
	// The port number field wasn't being set correctly.
	// It was written serv_addr.sin_prt instead of serv_addr.sin_port.
    serv_addr.sin_port         = htons(PORT);

    if (bind(listen_fd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("bind");
        exit(1);
    }

    if (listen(listen_fd, 8) < 0) {
        perror("listen");
        exit(1);
    }

    printf("server: listening on port %d\n", PORT);
    fflush(stdout);

    for (;;) {
        cli_len = sizeof(cli_addr);
        conn_fd = accept(listen_fd, (struct sockaddr *) &cli_addr, &cli_len);
        if (conn_fd < 0) {
            perror("accept");
            continue;
        }

        printf("server: client connected from %s:%d\n",
               inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));
        fflush(stdout);

	// The server was not reading the data from the client correctly, it only read the first part of the message.
	// I added a loop to read all the data from the client instead of only reading once and now it could read the entire message in packages of 1024 bytes.
	for (;;) {
		n = read(conn_fd, buffer, BUFSIZE);

		// If the client closes the connection while the server is reading, read() returns 0 
		// So we need to stop reading 
		if (n == 0) {
			break;  
		}

		// If read() returns a negative value, it indicates an error occurred during the read operation.
		if (n < 0) {
			perror("read");
			break;
		}

		printf("server: received %zd bytes\n", n);
		fflush(stdout);

		ssize_t sent = 0;

		// To ensure that all bytes are sent back to the client, we need to loop until all bytes are sent.
		while (sent < n) {
			ssize_t written = write(conn_fd, buffer + sent, (size_t)(n - sent));

			if (written <= 0) {
				perror("write");
				break;
			}

			sent += written;
		}

		// If sent < n, it means that the while loop fails (there's an error in the loop)
		// This only occurs if the write() call failed, so we need to break the loop and close the connection.
		if (sent < n) {
			break;
		}
	}


        close(conn_fd);
        printf("server: client disconnected\n\n");
        fflush(stdout);
    }

    return 0;
}