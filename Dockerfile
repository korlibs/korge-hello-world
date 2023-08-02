FROM gradle:8.2.1-jdk17-alpine as builder
WORKDIR /home/gradle/app
COPY --chown=gradle:gradle . /home/gradle/app
# Install necessary graphic and audio libraries
RUN apk add --no-cache freeglut-dev openal-soft-dev mesa-dri-gallium gcompat
RUN gradle jsBrowserDistribution

FROM node:18-alpine
WORKDIR /app
COPY --from=builder /home/gradle/app/build/distributions /app
RUN npm install -g http-server
EXPOSE 8080
CMD ["http-server", "-p", "8080"]
