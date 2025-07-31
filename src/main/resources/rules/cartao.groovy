score = 0
if (txValue > 50 && txValue <= 300) {
    score += 300
}
if (cpfAllow) {
    score -= 300
}

if (txValue == 1000) {
    score +=10000
}

return score;