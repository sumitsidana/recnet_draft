cd ~/cofactor/src
LANG=en_US.utf8
echo "preprocessing"
python preprocess_outbrain.py "/data/sidana/nnmf_ranking/recnet_draft/ml100k/cofactor"
echo "running cofactor"
python Cofactorization_outbrain.py "/data/sidana/nnmf_ranking/recnet_draft/ml100k/cofactor/pro"