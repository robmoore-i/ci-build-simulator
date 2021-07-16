if [ "$0" != "./docker/open-workspace.sh" ]; then
  echo "Run this script from the repository's root directory.
You are currently in a directory called '$(pwd | xargs basename)'."
  exit 1
fi

docker run -it -v "$(pwd)":/ci-build-simulator -v "$HOME/.ssh:/root/.ssh" ci-build-simulator bash