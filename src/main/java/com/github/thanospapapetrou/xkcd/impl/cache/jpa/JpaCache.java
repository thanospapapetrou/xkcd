package com.github.thanospapapetrou.xkcd.impl.cache.jpa;

import java.util.List;
import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;

import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cache.Cache;

/**
 * Class implementing a cache using JPA. Instances of this class are not thread-safe.
 * 
 * @author thanos
 */
@RequestScoped
public class JpaCache implements Cache {
	private static final String ERROR_LOADING_COMIC = "Error loading comic %1$d";
	private static final String ERROR_LOADING_LATEST_COMIC = "Error loading latest comic";
	private static final String ERROR_SAVING_COMIC = "Error saving comic %1$d";
	private static final String LOAD_LATEST = "SELECT c FROM Comic c ORDER BY c.id DESC";
	private static final String NULL_COMIC = "Comic must not be null";
	private static final String NULL_MANAGER = "Manager must not be null";

	private final EntityManager manager;

	/**
	 * Construct a new JPA cache.
	 * 
	 * @param manager
	 *            the entity manager to use
	 */
	@Inject
	public JpaCache(final EntityManager manager) {
		this.manager = Objects.requireNonNull(manager, NULL_MANAGER);
	}

	JpaCache() {
		// this constructor exists just to keep CDI happy
		manager = null;
	}

	@Override
	public Comic load(int id) throws XkcdException {
		try {
			manager.getTransaction().begin();
			final Comic comic = manager.find(Comic.class, id, LockModeType.PESSIMISTIC_READ);
			manager.getTransaction().commit();
			return comic;
		} catch (final PersistenceException e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new XkcdException(String.format(ERROR_LOADING_COMIC, id), e);
		}
	}

	@Override
	public Comic loadLatest() throws XkcdException {
		try {
			manager.getTransaction().begin();
			final List<Comic> comics = manager.createQuery(LOAD_LATEST, Comic.class).setMaxResults(1).getResultList();
			manager.getTransaction().commit();
			return comics.isEmpty() ? null : comics.get(0);
		} catch (final PersistenceException e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new XkcdException(ERROR_LOADING_LATEST_COMIC, e);
		}
	}

	@Override
	public void save(final Comic comic) throws XkcdException {
		Objects.requireNonNull(comic, NULL_COMIC);
		try {
			manager.getTransaction().begin();
			manager.persist(comic);
			manager.getTransaction().commit();
		} catch (final PersistenceException e) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			throw new XkcdException(String.format(ERROR_SAVING_COMIC, comic.getId()), e);
		}
	}
}
