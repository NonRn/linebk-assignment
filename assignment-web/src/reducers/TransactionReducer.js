import _ from 'lodash';

const initial = {
    transactions: [],
    loading: false,
    error: null,
};

const TransactionReducer = (state = initial, action) => {
    const nextState = _.cloneDeep(state);

    switch (action.type) {
        case 'TRANSACTION_FETCH_REQUEST':
            return { ...state, loading: true, error: null };
        case 'TRANSACTION_FETCH_SUCCESS':
            return { ...state, loading: false, transactions: action.payload || [] };
        case 'TRANSACTION_FETCH_FAILURE':
            return { ...state, loading: false, error: action.payload };
        case 'TRANSACTION_CLEAR':
            return { ...state, transactions: [], error: null };
        default:
            return nextState;
    }
};

export default TransactionReducer;
