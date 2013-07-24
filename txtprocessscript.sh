tr '/\\"()\-;' '      .' <descriptions.txt> d2
sed 's/\n/.\n/' d2
tr -s ' .' ' .' <d2> descriptions.txt
rm d2
