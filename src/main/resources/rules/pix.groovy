score = 0
// Custom for PIX
if (txValue > 50 && txValue <= 300) {
    score += 200
}
if (cpfAllow) {
    score -= 200
}

if (txValue == 101) {
    score +=1000
}

if (txValue == 102) {
    score -=1000
}

return score;