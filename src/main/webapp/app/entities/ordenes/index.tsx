import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ordenes from './ordenes';
import OrdenesDetail from './ordenes-detail';
import OrdenesUpdate from './ordenes-update';
import OrdenesDeleteDialog from './ordenes-delete-dialog';

const OrdenesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ordenes />} />
    <Route path="new" element={<OrdenesUpdate />} />
    <Route path=":id">
      <Route index element={<OrdenesDetail />} />
      <Route path="edit" element={<OrdenesUpdate />} />
      <Route path="delete" element={<OrdenesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrdenesRoutes;
