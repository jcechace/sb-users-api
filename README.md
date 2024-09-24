Simple Access Management API
===
This demo project is a simple implementation of a REST API for managing access grants to external projects. It served as a SpringBoot learning excersise.

## How to run
The iseasiest way to run the project is to use the provided docker-compose file. Only docker or podman (together with compose) is required.
From the project root directory run:

```bash
# using podman
podman compose -f src/docker/compose.yaml up
# using docker
docker compose -f src/docker/compose.yaml up
```


## Design and Implementation notes
Refer to the [design document](DESIGN.md) for more information on the design and implementation of the project.