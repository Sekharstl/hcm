import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { fetchCandidates } from '../../services/candidateService';

export interface Candidate {
  candidateId: string;
  tenantId: string;
  organizationId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  dateOfBirth: string;
  gender: string;
  nationality: string;
}

interface CandidateState {
  candidates: Candidate[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  totalElements: number;
  page: number;
  size: number;
}

const initialState: CandidateState = {
  candidates: [],
  loading: false,
  error: null,
  totalPages: 0,
  totalElements: 0,
  page: 0,
  size: 5,
};

export const fetchCandidatesThunk = createAsyncThunk(
  'candidates/fetchCandidates',
  async (params: { page?: number; size?: number; sortBy?: string; sortDirection?: 'ASC' | 'DESC' } = {}, { rejectWithValue }) => {
    try {
      const { page, size, sortBy, sortDirection } = params;
      const data = await fetchCandidates(page, size, sortBy, sortDirection);
      return data;
    } catch (err: any) {
      return rejectWithValue(err.message || 'Failed to fetch candidates');
    }
  }
);

const candidateSlice = createSlice({
  name: 'candidates',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchCandidatesThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCandidatesThunk.fulfilled, (state, action) => {
        state.loading = false;
        // Assume backend returns { content, totalPages, totalElements, page, size }
        state.candidates = action.payload.content || [];
        state.totalPages = action.payload.totalPages || 0;
        state.totalElements = action.payload.totalElements || 0;
        state.page = action.payload.page || 0;
        state.size = action.payload.size || 5;
      })
      .addCase(fetchCandidatesThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export default candidateSlice.reducer; 