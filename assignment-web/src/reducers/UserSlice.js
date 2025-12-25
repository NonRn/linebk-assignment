import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getUserByIdApi } from '../services/UserService';

const initialState = {
    item: null,
    status: 'idle',
    error: null,
};

export const fetchUserById = createAsyncThunk(
    'users/fetchById',
    async (userId, { rejectWithValue }) => {
        try {
            const { data } = await getUserByIdApi(userId);
            return data;
        } catch (error) {
            const message = error.response?.data?.message ?? error.message ?? 'Unable to fetch user';
            return rejectWithValue(message);
        }
    }
);

const userSlice = createSlice({
    name: 'users',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchUserById.pending, (state) => {
                state.status = 'loading';
                state.error = null;
            })
            .addCase(fetchUserById.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.item = action.payload;
            })
            .addCase(fetchUserById.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload ?? action.error?.message ?? 'Unknown error';
            });
    },
});

export const selectUser = (state) => state.users.item;
export const selectUserStatus = (state) => state.users.status;
export const selectUserError = (state) => state.users.error;

export default userSlice.reducer;