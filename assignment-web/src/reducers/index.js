import UserReducer from './UserReducer'
import {combineReducers} from 'redux'

export default combineReducers ({
    user:UserReducer
})