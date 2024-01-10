#!/bin/bash

# Get the list of directories
directories=$(find . -mindepth 1 -maxdepth 1 -type d)

# Loop over each directory
for dir in $directories; do
  # Check if deploy.sh exists in the directory
  if [[ -f $dir/deploy.sh ]]; then
    echo "Deploying service in directory: $dir"
    
    # Go into the directory
    cd $dir

    # Run the deploy.sh script
    ./deploy.sh

    # Go back to the parent directory
    cd ..

    echo "Finished deploying service in directory: $dir"
  fi
done

echo "Deployment of all services completed."
