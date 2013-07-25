tr '/\\"()\-;' '      .' <testdescriptions.txt> d2
sed 's/\n/.\n/' d2
sed 's/./ . /' d2
tr -s ' .' ' .' <d2> testdescriptions.txt
rm d2
