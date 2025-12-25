import _ from 'lodash'

const initial = {
    user: {},
}

const UserReducer = (state = initial, action) => {
    const nextState = _.cloneDeep(state);

    switch (action.type) {
        case "USER_DATA":
            return {...state, user: action.payload};
        case "USER_LOGOUT":
            return {...state, user: {}};
        default:
            return nextState
    }
}

export default UserReducer