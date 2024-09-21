start:
	 podman run --name postgres  \
 		-p 5432:5432 \
 		-e POSTGRES_USER=root \
 		-e POSTGRES_PASSWORD=root \
 		-d postgres:16-alpine
stop:
	podman stop postgres
	podman rm postgres
createdb:
	podman exec -it postgres createdb --username root --owner root sb_users

dropdb:
	podman exec -it postgres dropdb sb_users

.PHONY: postgres createdb dropdb