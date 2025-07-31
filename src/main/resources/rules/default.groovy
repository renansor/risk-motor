score = 0
if (txValue > 50 && txValue <= 300) {
    score += 200
} else if (txValue > 300 && txValue <= 5000) {
    score += 300
} else if (txValue > 5000 && txValue <= 20000) {
    score += 400
} else if (txValue > 20000) {
    score += 500
}
if (cpfAllow) {
    score -= 200
}
if (cpfDeny) {
    score += 400
}
if (ipDeny || deviceDeny) {
    score += 400
}

return score;