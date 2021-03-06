cd ~/cofactor/src
LANG=en_US.utf8
echo "preprocessing"
python preprocess.py "/data/sidana/recnet_draft/ml100k/cofactor"
echo "running cofactor"
python Cofactorization.py "/data/sidana/recnet_draft/ml100k/cofactor/pro"
cd ~/nnmf_ranking/
./writegroundtruthforcofactor.sh ml100k
./writepredictorforcofator.sh ml100k
./writerelevancevectorcofactor.sh ml100k one
./writerelevancevectorcofactor.sh ml100k five
./writerelevancevectorcofactor.sh ml100k ten

# For all items
# python Cofactorization.py /data/sidana/recnet_draft/kasandr/cofactor_all/pro/ and keep changing "k" in Cofactorization.py
