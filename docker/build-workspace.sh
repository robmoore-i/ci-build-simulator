if [ "$0" != "./docker/build-workspace.sh" ]; then
  echo "Run this script from the repository's root directory.
You are currently in a directory called '$(pwd | xargs basename)'."
  exit 1
fi

docker build -f docker/Dockerfile -t ci-build-simulator .