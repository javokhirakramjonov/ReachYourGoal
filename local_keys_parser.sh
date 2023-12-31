#!/bin/bash

localKeysParser() {
    file=$1
    while IFS= read -r line || [[ -n $line ]]; do
    if [[ $line == $'#'* ]]; then
        comment_len=1
        comment_in_kotlin="//"
        echo -e "\t$comment_in_kotlin$(echo "$line" | cut -c $(($comment_len+1))-)"
    elif [[ $line == *=* ]]; then
        actualKey=$(echo "$line" | cut -d'=' -f1)
        newKey=$(echo "$actualKey" | tr '.' '_' | tr 'a-z' 'A-Z')
        echo -e "\t$newKey(key = \"$actualKey\"),"
    else
        echo "$line"
    fi
    done < "$file"
}

project_root=$(pwd)
output_directory="$project_root/src/main/kotlin/me/javahere/reachyourgoal/localize"
output_file="$output_directory/MessagesEnum.kt"

mkdir -p $output_directory

if [ -f "$output_file" ]; then
    echo "MessagesEnum.kt already exists. Removing existing file."
    rm "$output_file"
fi

properties_file="$project_root/src/main/resources/messages/messages_en.properties"

result=$(localKeysParser "$properties_file")

cat <<EOL > $output_file
package me.javahere.reachyourgoal.localize

enum class MessagesEnum(val key: String) {
$result
}
EOL

echo "Enum has been created successfully at: $output_file"