// Candidate management service for API endpoints and CRUD operations

import api from './api';

const BASE_URL = 'http://10.19.5.50:30112/api/v1/candidates'; // Hardcoded NodePort backend URL

export const CANDIDATE_ENDPOINTS = {
  list: `${BASE_URL}`,
  get: (id: string) => `${BASE_URL}/${id}`,
  create: `${BASE_URL}`,
  update: (id: string) => `${BASE_URL}/${id}`,
  delete: (id: string) => `${BASE_URL}/${id}`,
};

// Example CRUD functions (using fetch, can be replaced with axios or other client)
export const fetchCandidates = async (page = 1, pageSize = 5, sortBy = '', sortOrder = '') => {
  let url = `${CANDIDATE_ENDPOINTS.list}?page=${page}&size=${pageSize}`;
  if (sortBy) url += `&sortBy=${sortBy}&sortOrder=${sortOrder}`;
  const res = await api.get(url);
  return res.data;
};

export const fetchCandidateById = async (id: string) => {
  const res = await api.get(CANDIDATE_ENDPOINTS.get(id));
  return res.data;
};

export const createCandidate = async (candidate: any) => {
  const res = await api.post(CANDIDATE_ENDPOINTS.create, candidate);
  return res.data;
};

export const updateCandidate = async (id: string, candidate: any) => {
  const res = await api.put(CANDIDATE_ENDPOINTS.update(id), candidate);
  return res.data;
};

export const deleteCandidate = async (id: string) => {
  const res = await api.delete(CANDIDATE_ENDPOINTS.delete(id));
  return res.data;
};

export const addCandidateSkill = async (candidateId: string, skill: any) => {
  const res = await api.post(`${BASE_URL}/${candidateId}/skills`, skill);
  return res.data;
};

export const addCandidateEducation = async (candidateId: string, education: any) => {
  const res = await api.post(`${BASE_URL}/${candidateId}/educations`, education);
  return res.data;
};

export const addCandidateWorkHistory = async (candidateId: string, workHistory: any) => {
  const res = await api.post(`${BASE_URL}/${candidateId}/work-histories`, workHistory);
  return res.data;
};

export const addCandidateCertification = async (candidateId: string, certification: any) => {
  const res = await api.post(`${BASE_URL}/${candidateId}/certifications`, certification);
  return res.data;
}; 
