/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.descartes.petstore.persistence.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import tools.descartes.petstore.entities.Order;
import tools.descartes.petstore.persistence.domain.OrderRepository;
import tools.descartes.petstore.rest.AbstractCRUDEndpoint;

/**
 * Persistence endpoint for for CRUD operations on orders.
 * @author Joakim von Kistowski
 *
 */
@Path("orders")
public class OrderEndpoint extends AbstractCRUDEndpoint<Order> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long createEntity(final Order order) {
		return OrderRepository.REPOSITORY.createEntity(order);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Order findEntityById(final long id) {
		Order order = OrderRepository.REPOSITORY.getEntity(id);
		if (order == null) {
			return null;
		}
		return new Order(order);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Order> listAllEntities(final int startIndex, final int maxResultCount) {
		List<Order> order = new ArrayList<Order>();
		for (Order o : OrderRepository.REPOSITORY.getAllEntities(startIndex, maxResultCount)) {
			order.add(new Order(o));
		}
		return order;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean updateEntity(long id, Order order) {
		return OrderRepository.REPOSITORY.updateEntity(id, order);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean deleteEntity(long id) {
		return OrderRepository.REPOSITORY.removeEntity(id);
	}
	
	/**
	 * Returns all order items with the given product Id (all order items for that product).
	 * @param userId The id of the product.
	 * @param startPosition The index (NOT ID) of the first order item with the product to return.
	 * @param maxResult The max number of order items to return.
	 * @return list of order items with the product.
	 */
	@GET
	@Path("user/{user:[0-9][0-9]*}")
	public List<Order> listAllForUser(@PathParam("user") final Long userId,
			@QueryParam("start") final Integer startPosition,
			@QueryParam("max") final Integer maxResult) {
		if (userId == null) {
			return listAll(startPosition, maxResult);
		}
		List<Order> orders = new ArrayList<Order>();
		for (Order o : OrderRepository.REPOSITORY.getAllEntitiesWithUser(userId,
				parseIntQueryParam(startPosition), parseIntQueryParam(maxResult))) {
			orders.add(new Order(o));
		}
		return orders;
	}

}
