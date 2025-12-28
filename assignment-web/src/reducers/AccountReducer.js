import _ from 'lodash';

const initial = {
    accounts: [],
    loading: false,
    error: null,
};

const AccountReducer = (state = initial, action) => {
    const nextState = _.cloneDeep(state);

    switch (action.type) {
        case 'ACCOUNT_FETCH_REQUEST':
            return { ...state, loading: true, error: null };
        case 'ACCOUNT_FETCH_SUCCESS':
            return { ...state, loading: false, accounts: action.payload || [] };
        case 'ACCOUNT_FETCH_FAILURE':
            return { ...state, loading: false, error: action.payload };
        case 'ACCOUNT_CLEAR':
            return { ...state, accounts: [], error: null };
        default:
            return nextState;
    }
};

export default AccountReducer;
