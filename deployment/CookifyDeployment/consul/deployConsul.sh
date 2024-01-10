#!/bin/bash

# Get the list of directories
directories=$(find . -mindepth 1 -maxdepth 1 -type d)

# Loop over each directory
for dir in $directories; do
  # Get the list of scripts that start with 'deploy' and end with '.sh'
  scripts=$(find $dir -name 'deploy*.sh')

  # Loop over each script
  for script in $scripts; do
    # Check if the script is a file
    if [[ -f $script ]]; then
      echo "Running script: $script"
      
      # Make the script executable
      chmod +x $script

      # Run the script
      ./$script

      echo "Finished running script: $script"
    fi
  done
done

echo "Execution of all scripts completed."
