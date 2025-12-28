import _ from 'lodash';

const initial = {
    banners: [],
    loading: false,
    error: null,
};

const BannerReducer = (state = initial, action) => {
    const nextState = _.cloneDeep(state);

    switch (action.type) {
        case 'BANNER_FETCH_REQUEST':
            return { ...state, loading: true, error: null };
        case 'BANNER_FETCH_SUCCESS':
            return { ...state, loading: false, banners: action.payload || [] };
        case 'BANNER_FETCH_FAILURE':
            return { ...state, loading: false, error: action.payload };
        case 'BANNER_CLEAR':
            return { ...state, banners: [], error: null };
        default:
            return nextState;
    }
};

export default BannerReducer;
