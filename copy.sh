cat all-data-jam.csv | egrep '^BL|, BL,' > paper/BL/applicant.csv
cat all-data-jam.csv | egrep '^BT|, BT,' > paper/BT/applicant.csv
cat all-data-jam.csv | egrep '^CY|, CY,' > paper/CY/applicant.csv
cat all-data-jam.csv | egrep '^GG|, GG,' > paper/GG/applicant.csv
cat all-data-jam.csv | egrep '^MS|, MS,' > paper/MS/applicant.csv
cat all-data-jam.csv | egrep '^PH|, PH,' > paper/PH/applicant.csv
cat all-data-jam.csv | egrep '^MA|, MA,' > paper/MA/applicant.csv


cat paper/PH/applicant.csv | cut -d , -f3-14 > paper/PH/applicant1.csv
cat paper/MA/applicant.csv | cut -d , -f3-14 > paper/MA/applicant1.csv
cat paper/MS/applicant.csv | cut -d , -f3-14 > paper/MS/applicant1.csv
cat paper/GG/applicant.csv | cut -d , -f3-14 > paper/GG/applicant1.csv
cat paper/CY/applicant.csv | cut -d , -f3-14 > paper/CY/applicant1.csv
cat paper/BL/applicant.csv | cut -d , -f3-14 > paper/BL/applicant1.csv
cat paper/BT/applicant.csv | cut -d , -f3-14 > paper/BT/applicant1.csv

mv paper/PH/applicant1.csv paper/PH/applicant.csv
mv paper/MA/applicant1.csv paper/MA/applicant.csv
mv paper/MS/applicant1.csv paper/MS/applicant.csv
mv paper/GG/applicant1.csv paper/GG/applicant.csv
mv paper/CY/applicant1.csv paper/CY/applicant.csv
mv paper/BL/applicant1.csv paper/BL/applicant.csv
mv paper/BT/applicant1.csv paper/BT/applicant.csv
