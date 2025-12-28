import _ from 'lodash';

const initial = {
    debitCards: [],
    loading: false,
    error: null,
};

const DebitCardReducer = (state = initial, action) => {
    const nextState = _.cloneDeep(state);

    switch (action.type) {
        case 'DEBITCARD_FETCH_REQUEST':
            return { ...state, loading: true, error: null };
        case 'DEBITCARD_FETCH_SUCCESS':
            return { ...state, loading: false, debitCards: action.payload || [] };
        case 'DEBITCARD_FETCH_FAILURE':
            return { ...state, loading: false, error: action.payload };
        case 'DEBITCARD_CLEAR':
            return { ...state, debitCards: [], error: null };
        default:
            return nextState;
    }
};

export default DebitCardReducer;
