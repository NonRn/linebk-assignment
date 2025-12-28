const currencySign = (currency = 'THB') => {
    switch (currency) {
        case 'THB':
            return '฿';
        case 'USD':
            return '$';
        case 'EUR':
            return '€';
        case 'JPY':
        case 'CHN':
            return '¥';
        default:
            return '';
    }
}

const convertCurrencyRate = (amount, currency = 'THB') => {
    if (!amount || isNaN(amount)) return 0;
    
    switch (currency) { //mock exchange rate
        case 'THB':
            return amount * 1;
        case 'USD':
            return amount * 32.1;
        case 'EUR':
            return amount * 36.6;
        case 'JPY':
            return amount * 0.2;
        case 'CHN':
            return amount * 4.42;
        default:
            return amount * 1;
    }
}

const maskingDebitCard = (cardNumber = '', showStart = 0, showEnd = 0) => {
    if (cardNumber.length === 0) return '';

    // masking โดยไม่สนใจช่องว่าง
    const cardNumberNoSpaces = cardNumber.replace(/\s+/g, '');
    const length = cardNumberNoSpaces.length;
    
    // masking ตำแหน่ง
    const start = cardNumberNoSpaces.slice(0, showStart);
    const end = cardNumberNoSpaces.slice(length - showEnd);
    const maskedLength = length - (showStart + showEnd);
    const middle = maskedLength > 0 ? '•'.repeat(maskedLength) : '';
    const maskedNoSpaces = `${start}${middle}${end}`;
    
    // เติมช่องว่างกลับเข้าตำแหน่งเดิม
    let result = '';
    let maskedIndex = 0;
    
    for (let i = 0; i < cardNumber.length; i++) {
        if (cardNumber[i] === ' ') {
            result += ' ';
        } else {
            result += maskedNoSpaces[maskedIndex];
            maskedIndex++;
        }
    }
    
    return result;
}

export default {
    currencySign,
    maskingDebitCard,
};